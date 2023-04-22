//#include <iostream>
#include <vector>

using namespace std;

int countPrimes(int n) {
	vector<bool> primes(n - 2, true);
	for(int i = 2; i < n;) {
		for(int j = i * 2; j < n; j += i) {
			primes[j - 2] = false;
		}
		for(i++; !primes[i - 2] && i < n; i++);
	}
	int count = 0;
	for(int i = 0; i < n - 2; i++) {
		if(primes[i]) count++;
	}
	return count;
}

//int main() {
//	cout << countPrimes(10) << " = 4\n";
//	cout << countPrimes(0) << " = 0\n";
//	cout << countPrimes(2) << " = 0\n";
//	cout << countPrimes(100000) << " = 9592\n";
//	cout << countPrimes(5000000) << " = 348513\n";
//	return 0;
//}