unsigned long long NOD(unsigned long long a, unsigned long long b) {
	while(true) {
		if (a % b == 0) {
			return b;
		} else if (b % a == 0) {
			return a;
		} else if (a > b) {
			a = a % b;
		} else {
			b = b % a;
		}
	}
	return -1;
}

bool coprime(unsigned long long a, unsigned long long b) {
	return NOD(a, b) == 1;
}

//int main() {
//	std::cout << coprime(8, 20) << " = false\n";
//	std::cout << coprime(462, 1071) << " = false\n";
//	std::cout << coprime(100000000, 999999999) << " = true\n";
//	std::cout << coprime(524287, 1125897758834689LL) << " = false\n";
//	return 0;
//}