#pragma once
map<string, string> variables;
string eval(string expr);

bool isTrue(string condition) { return eval(condition) != "0"; }

bool isNumber(const string& s) {
    std::string::const_iterator it = s.begin();
    while (it != s.end() && (std::isdigit(*it) || *it == '.')) ++it;
    return !s.empty() && it == s.end();
}

string evalLeft(const string& s, int index);
string evalRight(const string& s, int index);
double evalLeftd(const string& s, int index);
double evalRightd(const string& s, int index);
bool evalLeftb(const string& s, int index);
bool evalRightb(const string& s, int index);

string eval(string expr) {
    if(expr.empty()) return "";
    cout << expr << endl;
    if(expr[0] == '\'' && expr[expr.length() - 1] == '\'' && expr.substr(1, expr.length() - 2).find('\'') == std::string::npos) return expr.substr(1, expr.length() - 2);
    try {
        string spacelessExpr;  // Get Rid of Spaces
        for (int i = 0; i < expr.length(); i++) {
            if (expr[i] == '\'') {
                i++;
                string token;
                while (true) {
                    if (expr[i] == '\\') {
                        i++;
                        token += expr[i];
                    } else if (expr[i] == '\'') {
                        break;
                    } else {
                        token += expr[i];
                    }
                    i++;
                }
                spacelessExpr += "'" + token + "'";
            } else if (expr[i] != ' ') {
                spacelessExpr += expr[i];
            }
        }

        string tok = "";  // Do parantheses first
        for (int i = 0; i < spacelessExpr.length(); i++) {
            if (spacelessExpr[i] == '(') {
                int iter = 1;
                string token;
                i++;
                while (true) {
                    if (spacelessExpr[i] == '(') {
                        iter++;
                    } else if (spacelessExpr[i] == ')') {
                        iter--;
                        if (iter == 0) {
                            i++;
                            break;
                        }
                    }
                    token += spacelessExpr[i];
                    i++;
                }
                tok += eval(token);
            }
            tok += spacelessExpr[i];
        }

        for (int i = 0; i < tok.length(); i++) {
            if (tok[i] == '+') {
                if (isNumber(evalLeft(tok, i)) && isNumber(evalRight(tok, i))) {
                    return to_string(evalLeftd(tok, i) + evalRightd(tok, i));
                } else {
                    return evalLeft(tok, i) + evalRight(tok, i);
                }
            } else if (tok[i] == '-') {
                return to_string(evalLeftd(tok, i) - evalRightd(tok, i));
            }
        }

        for (int i = 0; i < tok.length(); i++) {
            if (tok[i] == '*') {
                return to_string(evalLeftd(tok, i) * evalRightd(tok, i));
            } else if (tok[i] == '/') {
                return to_string(evalLeftd(tok, i) / evalRightd(tok, i));
            }
        }

        for (int i = 0; i < tok.length(); i++) {
            if (tok.substr(i, 2) == "&&") {
                return to_string(evalLeftb(tok, i) && evalRightb(tok, i + 1));
            } else if (tok.substr(i, 2) == "||") {
                return to_string(evalLeftb(tok, i) || evalRightb(tok, i + 1));
            }
        }

        for (int i = 0; i < tok.length(); i++) {
            if (tok[i] == '>') {
                return to_string(evalLeftd(tok, i) > evalRightd(tok, i));
            } else if (tok[i] == '<') {
                return to_string(evalLeftd(tok, i) < evalRightd(tok, i));
            } else if (tok.substr(i, 2) == "==") {
                cout << tok << endl;
                cout << evalLeft(tok, i) << " == " << evalRight(tok, i + 1) << endl;
                return to_string(evalLeft(tok, i) == evalRight(tok, i + 1));
            }
        }

        if (tok.substr(0, 1) == "!") {
            return to_string(!isTrue(eval(tok.substr(1))));
        }

        return tok;  // Return the value...
    } catch (...) {
        fprintf(stderr, "Unable to evaluate expression %s!\n", expr.c_str());
        return "-1";
    }
}

string evalLeft(const string& s, int index) { return eval(s.substr(0, index)); }
string evalRight(const string& s, int index) { return eval(s.substr(index + 1, s.length() - index - 1)); }
double evalLeftd(const string& s, int index) { return stod(evalLeft(s, index)); }
double evalRightd(const string& s, int index) { return stod(evalRight(s, index)); }
bool evalLeftb(const string& s, int index) { return isTrue(evalLeft(s, index)); }
bool evalRightb(const string& s, int index) { return isTrue(evalRight(s, index)); }
