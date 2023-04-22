#pragma once

using namespace std;
const char* spaces = " \t\n\r\f\v";
const char* varnameAllowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_[].#";

bool cmdOptionExists(char** begin, char** end, const string& option) { return find(begin, end, option) != end; }

char* getCmdOption(char** begin, int argc, const string& option) {
    char** itr = find(begin, begin + argc, option);
    if (itr != begin + argc && ++itr != begin + argc) {
        return *itr;
    }
    return 0;
}

char* getCmdOptionOrDefault(char** begin, int argc, const string& option, char* defaultValue) {
    char* found = getCmdOption(begin, argc, option);
    if (found == 0) {
        return defaultValue;
    }
    return found;
}

string readFile(string filename) {
    char* buffer = 0;
    long length;
    FILE* f = fopen(filename.c_str(), "rb");

    if (f) {
        fseek(f, 0, SEEK_END);
        length = ftell(f);
        fseek(f, 0, SEEK_SET);
        buffer = new char[length + 1];
        if (buffer) {
            fread(buffer, 1, length, f);
        }
        buffer[length] = '\0';
        fclose(f);
    }

    if (!buffer) {
        printf("Unable to open file %s!\n", filename.c_str());
        exit(-1);
    }
    return string(buffer);
}

void trim(string& s) {
    s.erase(s.find_last_not_of(spaces) + 1);
    s.erase(0, s.find_first_not_of(spaces));
}

vector<string> split(const string& s, char delim, bool trimEach = false) {
    vector<string> elems;
    string item;
    bool quoted = false, backslashed = false;
    for (char c : s) {
        if (backslashed) {
            item += c;
            backslashed = false;
            continue;
        }
        if (c == '\\') {
            backslashed = true;
        } else if (c == delim && !quoted) {
            if (trimEach) trim(item);
            if (!item.empty()) elems.push_back(item);
            item = "";
        } else if (c == '"') {
            quoted = !quoted;
        } else {
            item += c;
        }
    }
    if (trimEach) trim(item);
    if (!item.empty()) elems.push_back(item);
    return elems;
}

void replace(string& str, string what, string replacement) {
    int iLength = what.length();
    size_t index = 0;
    while (true) {
        index = str.find(what, index);
        if (index == std::string::npos) {
            break;
        }
        str.replace(index, iLength, replacement);
        index += iLength;
    }
}

template <typename K, typename V>
V getOrDefault(const std::map<K, V>& m, const K& key, const V& defval) {
    typename std::map<K, V>::const_iterator it = m.find(key);
    return it == m.end() ? defval : it->second;
}
