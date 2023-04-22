#include <vector>

using namespace std;

vector<unsigned int> factor(unsigned int n) {
	vector<unsigned int> primes;
	primes.push_back(n);
	int f = 2;
	while(n != 1) {
		while(n % f == 0) {
			primes.push_back(f);
			n /= f;
		}
		f++; // works because: 9 = 3 * 3 (already divided by 3)
	}
	return primes;
}

vector<unsigned int> primesFactors (const vector<unsigned int>& numbers) {
	vector<unsigned int> primes;
	for(unsigned int n : numbers) {
		vector<unsigned int> primes2 = factor(n);
		if(primes.size() < primes2.size()) {
			primes = primes2;
		} else if(primes.size() == primes2.size() && primes2[0] < primes[0]) {
			primes = primes2;
		}
	}
	return primes;
}