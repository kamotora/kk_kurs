int main(){
   int a = 2 + 9;
   cout << a;
   a = 3;
   cout << a;
   a = a + 56;
   long long g = 3;

   short b = a + g;
   cout << b;
   if (b < 1) {
       cout << 100 * 2;
       a = 4;
   } else {
       cout << 200 / 3;
   }
   if (a == 4) {
       cout << 111;
   }
}