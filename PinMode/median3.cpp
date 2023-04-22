int median3(int a, int b, int c) {
	int a1 = a > b ? b : a;
	int b1 = b > c ? c : (a > b ? a : b);
	return a1 > b1 ? a1 : b1;
}