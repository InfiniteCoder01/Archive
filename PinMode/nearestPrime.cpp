#include <math.h>
#include <stdio.h>

unsigned long long nearestPrime(unsigned long long limit) { 
    unsigned long long prime = limit;
    if (prime % 2 == 0) {// 2 is only prime number thats not odd
        prime--;
    } else {
    	prime -= 2;// we need less than given limit
	}

    for (prime; prime >= 2; prime -= 2) {// try all possible combinations with non-odd numbers
    	bool f = true;
        for (unsigned long long i = 3; i <= sqrt(prime); i += 2) {// check if number is prime
            if (prime % i == 0) {// non-prime(complex)
            	f = false;
                break;
			}
        }
        if (f) {
            return prime;// is prime!!!
		}
    }

    return 2;// is 2
}//nearestPrime(1'693'182'318'747'503LL); tooks 3 sec... IMPOSIBLE

int main() {
	printf("%l", nearestPrime(1'693'182'318'747'503LL));
}