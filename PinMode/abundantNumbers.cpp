#include <vector>

using namespace std;

bool isAbundant(unsigned int n) {
	unsigned int divSum = 0;
	for(unsigned int i = 1; i < n; i++) {
		if(n % i == 0) {
			divSum += i;
		}
	}
	return divSum > n;
}

vector<unsigned int> abundantNumbers(const vector<unsigned int>& numbers) {
	vector<unsigned int> result;
	for(unsigned int n : numbers) {
		if(isAbundant(n)) {
			result.push_back(n);
		}
	}
	return result;
}