## CALL-BY-?

1. Scala normally uses call-by-value

2. But if the type of a function parameter starts with => it uses call-by-name

   ```scala
   def constOne(x: Int, y: => Int) = 1
   ```

   ​

3. `def` form is "by-name",`val` form is "by-value"

4. 一个理解by-name和by-value的例子

   ```scala
   def loop: Boolean = loop

   def x = loop // just another name
   val x = loop // lead an infinite loop

   // write functions `and` `or` 
   // and(x, y) == x && y
   // or(x, y) == x || y

   def and(x: Boolean, y: Boolean) = 
   	if (x) y else false

   and(true, false) // ok
   and(true, true) // ok
   and(true, loop) // lead an infinite loop

   // correct it using =>, call-by-name
   def and(x: Boolean, y: => Boolean) = 
   	if (x) y else false
   ```

## SQRT FUCTION

1. sqrt function: my version

   ```scala
   // my implemetation
   def sqrt(x: Double) = {
   	var y = 1.0
   	while (Math.abs(y * y - x) > 0.0001) {y = (y + x / y) / 2}
   	y
   }
   ```

2. sqrt function: martin version one

   ```scala
   // martin implemetation one:
   // note good for sqrt(1e-6) sqrt(1e60)
   // recursive function need an explicit return type,
   // for non-recursive function, the return type is optional

   def abs(x: Double) = if (x < 0) -x else x

   def isGoodEnough(guess: Double, x: Double) =
     abs(guess * guess - x) < 0.0001

   def improve(guess: Double, x: Double) =
     (guess + x / guess) / 2


   def sqrtIter(guess: Double, x: Double): Double =
     if (isGoodEnough(guess, x)) guess
     else sqrtIter(improve(guess, x), x)

   def sqrt(x: Double) = sqrtIter(1.0, x)
   ```

3. sqrt function: martin version two

   ```scala
   // martin implemetation two:
   def abs(x: Double) = if (x < 0) -x else x

   def isGoodEnough(guess: Double, x: Double) =
     abs(guess * guess - x) / x < 0.0001 // add / x here

   def improve(guess: Double, x: Double) =
     (guess + x / guess) / 2


   def sqrtIter(guess: Double, x: Double): Double =
     if (isGoodEnough(guess, x)) guess
     else sqrtIter(improve(guess, x), x)

   def sqrt(x: Double) = sqrtIter(1.0, x)
   ```

4. sqrt function: martin version three

   ```scala
   // martin implementation three: block version, rm some x
   def sqrt(x: Double) = {
     def abs(x: Double) = if (x < 0) -x else x

     def isGoodEnough(guess: Double) =
       abs(guess * guess - x) / x < 0.0001

     def improve(guess: Double) =
       (guess + x / guess) / 2


     def sqrtIter(guess: Double): Double =
       if (isGoodEnough(guess)) guess
       else sqrtIter(improve(guess))

     sqrtIter(1.0)
   }
   // block problem
   val x = 0
   def f(y: Int) = y + 1
   val result = {
       val x = f(3)
       x * x
   } + x
   // my answer is 16
   ```

## TAIL RECURSIVE

1. gcd is tail-recursive, factorial is not

   ```scala
   def gcd(a: Int, b: Int): Int = 
   	if (b == 0) a else gcd(b, a % b)
   // gcd(14, 21)
   // if (21 == 0) 14 else gcd(21, 14 % 21)
   // gcd(21, 14)
   // gcd(14, 7)
   // gcd(7, 0)
   // 7

   def factorial(n: Int): Int = 
   	if (n == 1) n else n * factorial(n - 1)
   // factorial(4)
   // 4 * factorial(3)
   // 4 * 3 * factorial(2)
   // 4 * 3 * 2 * factorial(1)
   // 4 * 3 * 2 * 1
   // 24

   // factorial tail-recursive verison
   def factorial(n: Int): Int = {
     def loop(acc: Int, n: Int): Int =
       if (n == 0) acc
       else loop(acc * n, n - 1)
     loop(1, n)
   }
   ```

2. tail recursive use constant stack space