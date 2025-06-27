#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <unistd.h>
#include <termios.h>
#include <cstdlib>
#include <csignal>
#include <filesystem>

namespace fs = std::filesystem;

const std::string jarName = "tickitakytoe_embedded.jar";
const std::string tempPath = "/tmp/" + jarName;

struct TermControl {
    struct termios orig;
    TermControl() {
        tcgetattr(STDIN_FILENO, &orig);
        struct termios raw = orig;
        raw.c_lflag &= ~(ICANON | ECHO);
        tcsetattr(STDIN_FILENO, TCSANOW, &raw);
    }
    ~TermControl() {
        tcsetattr(STDIN_FILENO, TCSANOW, &orig);
    }
};

void extractEmbeddedJar(const char* selfPath) {
    std::ifstream file(selfPath, std::ios::binary);
    if (!file) {
        std::cerr << "[ERROR] Cannot open self\n";
        std::exit(1);
    }

    // Scan for the marker at end
    file.seekg(0, std::ios::end);
    size_t size = file.tellg();

    // Footer is: "==JAR==\n" + 4-byte jar size
    constexpr size_t markerLen = 8;
    file.seekg(size - (markerLen + 4));

    char marker[markerLen];
    file.read(marker, markerLen);
    if (std::string(marker, markerLen) != "==JAR==\n") {
        std::cerr << "[ERROR] Embedded jar marker not found\n";
        std::exit(1);
    }

    uint32_t jarSize = 0;
    file.read(reinterpret_cast<char*>(&jarSize), 4);

    size_t jarOffset = size - (markerLen + 4 + jarSize);
    file.seekg(jarOffset);

    std::vector<char> jarData(jarSize);
    file.read(jarData.data(), jarSize);

    std::ofstream out(tempPath, std::ios::binary);
    out.write(jarData.data(), jarSize);
    out.close();
}

int runJava() {
    std::string cmd = "java -jar " + tempPath;
    return std::system(cmd.c_str());
}

int main(int argc, char** argv) {
    signal(SIGINT, [](int){ std::exit(1); });

    TermControl term;
    extractEmbeddedJar(argv[0]);
    int result = runJava();

    fs::remove(tempPath);
    return result;
}
