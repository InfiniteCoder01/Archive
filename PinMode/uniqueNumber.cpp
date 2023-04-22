#include <assert.h>
//#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int uniqueNumber(const vector<int>& vec) {
	vector<int> numbers;
	for(int n : vec) {
		if(!count(numbers.begin(), numbers.end(), n)) {
			numbers.push_back(n);
		} else {
			numbers.erase(find(numbers.begin(), numbers.end(), n));
		}
	}
	assert(numbers.size() == 1 && "Unique number should be single!");
	return numbers[0];
}

//int main() {
//	cout << uniqueNumber({1, 2, 3, 3, 10, 1, 2}) << " = 10\n";
//	cout << uniqueNumber({40, -20, 40}) << " = -20\n";
//	cout << uniqueNumber({0, 1, 1, 100, 100}) << " = 0\n";
//	cout << uniqueNumber({-8,-8, -1, -1, 1000}) << " = 1000\n";
//}