/**************************TASK A**************************/
//#include <stdlib.h>
//#include <iostream>
//#include <limits>
//
//using namespace std;
//
//int main() {
//	unsigned long long a, b, x, r = numeric_limits<unsigned long long>::max();
//	cin >> a;
//	cin >> b;
//	cin >> x;
//	for(unsigned long long n = 0; n < x / a + 1; n++) {
//		for(unsigned long long k = 0; k < x / b + 1; k++) {
//			if(a * n + b * k == x) {
//				r = __min(r, n + k);
//			}
//		}
//	}
//	if(r == numeric_limits<unsigned long long>::max()) {
//		cout << -1;
//		return 0;
//	}
//	cout << r;
//	return 0;
//}

/**************************TASK B**************************/
//#include <stdlib.h>
//#include <iostream>
//#include <limits>
//
//using namespace std;
//
//int main() {
//	long n, k, maxSum = numeric_limits<long>::min();
//	cin >> n;
//	cin >> k;
//	long arr[n];
//	for(long i = 0; i < n; i++) {
//		cin >> arr[i];
//	}
//	for(long i = 0; i < n - k + 1; i++) {
//		long sum = 0;
//		for(long j = i; j < k + i; j++) {
//			sum += arr[j];
//		}
//		maxSum = __max(maxSum, sum);
//	}
//	cout << maxSum;
//	return 0;
//}

/**************************TASK C**************************/
//#include <stdlib.h>
//#include <iostream>
//#include <limits>
//
//#define toNumber(i1, i2, i3) i1 * 100 + i2 * 10 + i3
//#define isStar(letter) letter == '*'
//#define toDigit(letter) letter - '0'
//#define forDigit(i, letter) for(int i = (isStar(letter) ? 9 : toDigit(letter)); i >= (isStar(letter) ? 1 : toDigit(letter)); i--)
//#define printNumbers(i1, i2, i3, i4, i5, i6) cout << toNumber(i1, i2, i3) << " " << toNumber(i4, i5, i6) << "\n"
//
//using namespace std;
//
//int main() {
//	char a_str[4], b_str[4];
//	cin.getline(a_str, 4, '\n');
//	cin.getline(b_str, 4, '\n');
//	int a, b, raznitsa = 9999999;
//	forDigit(i1, a_str[0]) {
//		forDigit(i2, a_str[1]) {
//			forDigit(i3, a_str[2]) {
//				forDigit(i4, b_str[0]) {
//					forDigit(i5, b_str[1]) {
//						forDigit(i6, b_str[2]) {
////							printNumbers(i1, i2, i3, i4, i5, i6);
//							int aTest, bTest, razn;
//							aTest = toNumber(i1, i2, i3);
//							bTest = toNumber(i4, i5, i6);
//							razn = abs(aTest - bTest);
//							if(raznitsa >= razn) {
//								a = aTest;
//								b = bTest;
//								raznitsa = razn;
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//	cout << raznitsa;
//	cout << " ";
//	cout << a;
//	cout << " ";
//	cout << b;
//	return 0;
//}

/**************************TASK D**************************/
#include <stdlib.h>
#include <iostream>
#include <limits>

using namespace std;

bool isPrime(long n) {
	if(n == 1) {
		return false;
	}

	for (long i = 2; i <= n / 2; i++) {
        if (n % i == 0) {
            return false;
        }
    }
    return true;
}

int main() {
	unsigned long long n, sum;
	cin >> n;
	for(int i = 1; i <= n; i++) {
		if(n % i == 0) {
			if(!isPrime(i)) {
				sum += i;
			}
		}
	}
	cout << sum;
	return 0;
}