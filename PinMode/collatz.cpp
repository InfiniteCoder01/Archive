int collatz(int n) {
	unsigned long long curr = n;
	int steps = 0;
	while(curr != 1) {
		if(curr % 2 == 0) {
			curr /= 2;
		} else {
			curr = curr * 3 + 1;
		}
		steps++;
	}
	return steps;
}