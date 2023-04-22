#define _CRT_SECURE_NO_WARNINGS
#include <algorithm>
#include <stdio.h>
#include <iostream>
#include <sstream>
#include <vector>
#include <map>
#include "io.hpp"
#include "eval.hpp"

void runFile(string filename);

vector<string> parseCommandArgs(string& command, string& sArgs) {
    vector<string> args = split(command, ' ', true);
    sArgs = command.substr(args[0].length() + 1);
    command = args[0];
    args.erase(args.begin());
    return args;
}

bool execute(string command) {
    static int iter = 0;
    trim(command);
    if (command.empty() || command.substr(0, 2) == "//") {
        return false;
    }

    for (size_t i = 0; i < command.length(); i++) {
        if (command[i] == '$') {
            string varname = command.substr(i + 1, command.substr(i + 1).find_first_not_of(varnameAllowed));
            command.replace(i, varname.length() + 1, getOrDefault(variables, varname, string("undefined")));
        }
    }

    // for (pair<string, string> variable : variables) {
    //     replace(command, "$" + variable.first, variable.second);
    // }
    string sArgs;
    vector<string> args = parseCommandArgs(command, sArgs);
    if (iter > 0 && !(command == "else" && iter == 1)) {
        if (command == "if")
            iter++;
        else if (command == "endif")
            iter--;
        return false;
    }
    if (command == "exit") {
        return true;
    } else if (command == "echo") {
        if (args.size() == 0) {
            cout << "Use: \"echo expression\"\n";
            return false;
        }
        cout << eval(sArgs) << endl;
    } else if (command == "exec") {
        if (args.size() > 0) {
            system(sArgs.c_str());
        } else {
            cout << "Use: \"exec program [arguments]\"\n";
            return false;
        }
    } else if (command == "call") {
        if (args.size() == 0) {
            cout << "Use: \"call script\", all variables will be transfered to this script\n";
            return false;
        }
        runFile(sArgs);
    } else if (args.size() == 2 && args[0] == "=") {
        variables[command] = eval(sArgs.substr(args[0].length() + 1));
    } else if (command == "if") {
        if (args.size() == 0) {
            cout << "Use: \"if expression\"\n";
            return false;
        }
        if (!isTrue(eval(sArgs))) {
            iter = 1;
        }
    } else if (command == "else") {
        iter = !iter;
    } else if (command == "endif") {
    }
    return false;
}

void runFile(string filename) {
    string text = readFile(filename);
    string command = "";
    int index = 0;
    while (true) {
        if (text[index] == '\n' || text[index] == '\0') {
            if (execute(command)) return;
            command = "";
            index++;
            if (text[index - 1] == '\0') return;
        } else if (text[index] == '\\') {
            command += text[++index];
            index++;
        } else {
            command += text[index++];
        }
    }
}

int main(int argc, char** argv) {
    if (argc == 1) {
        string command = "";
        printf(">>> ");
        while (true) {
            char c;
            scanf("%c", &c);
            if (c == '\n') {
                if (execute(command)) return 0;
                printf(">>> ");
                command = "";
            } else {
                command += c;
            }
        }
    } else {
        string filename = argv[1];
        for (int i = 2; i < argc; i++) {
            if (argv[i][0] == '-') {
                variables["cmd." + string(argv[i] + 1)] = (argc - i == 1) || argv[i + 1][0] == '-' ? "1" : string(argv[i + 1]);
            }
        }
        runFile(filename);
    }
    return 0;
}
