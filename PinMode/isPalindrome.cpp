#include <iostream>

using namespace std;

bool isPalindrome(long long n) {
	if(n < 0) {
		return false;
	}
	char n2 = 0;
	for(char i = 1; i < n; i *= 10) {
		n2 *= 10;
		n2 += (n / i) % 10;
	}
	return n == n2;
}

int main() {
//	cout << isPalindrome(123321) << " = true\n";
	cout << isPalindrome(125) << " = true\n";
//	cout << isPalindrome(545454) << " = false\n";
//	cout << isPalindrome(8888888) << " = true\n";
//	cout << isPalindrome(-100001) << " = false\n";
	return 0;
}