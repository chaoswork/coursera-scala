## HIGH ORDER FUNCTION

1. example

   ```scala
   def sumInts(a: Int, b: Int): Int = 
   	if (a > b) 0 else a + sumInts(a + 1, b)

   def cube(x: Int): Int = x * x * x

   def sumCubes(a: Int, b: Int): Int = 
   	if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

   def sumFactorials(a: Int, b: Int): Int = 
   	if (a > b) 0 else fact(a) + sumFactorials(a + 1, b)
   ```

   ​

2. summing with higher-order functions

   ```scala
   def sum(f: Int => Int, a: Int, b: Int): Int = 
   	if (a > b) 0 else f(a) + sum(f, a + 1, b)

   def sumInts(a: Int, b: Int) = sum(id, a, b)
   def sumCubes(a: Int, b: Int) = sum(cube, a, b)
   df sumFactorials(a: Int, b: Int) = sum(fact, a, b)
   ```

3. Anonymous Funtions are Syntactic Sugar

   ```scala
   // (x1: T1, ..., xn:Tn) => E can always be expressed using def as follows

   {def f(x1: T1, ..., xn:Tn) = E; f}

   def sumInts(a: Int, b: Int) = sum(x => x, a, b)
   def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)
   def sumFactorials(a: Int, b: Int) = sum(fact, a, b)
   ```

4. Write a tail-recursive version of sum

   ```scala
   def sum(f: Int => Int, a: Int, b: Int): Int = {
       def loop(a: Int, acc: Int): Int = {
           if (a > b) acc
           else loop(a + 1, acc + f(a))
       }
       loop(a, 0)
   }
   ```

   ​

5. function return function

   ```scala
   def sum(f: Int => Int): (Int, Int) => Int = {
       def sumF(a: Int, b: Int): Int = 
       	if (a > b) 0 else f(a) + sumF(a + 1, b)
       sumF
   }
   // so now
   def sumInts(a: Int, b: Int) = sum(x => x)
   def sumCubes(a: Int, b: Int) = sum(x = > x * x * x)
   def sumFactorials(a: Int, b: Int) = sum(fact)

   sum(cube)(1, 10)
   ```

6. Multiple Parameter Lists

   ```scala
   // this sum is a shorter version of above
   def sum(f: Int => Int)(a: Int, b: Int): Int = 
   	if (a > b) 0 else f(a) + sum(f)(a + 1, b)
   // type of sum is (Int => Int) => ((Int, Int) => Int)
   ```

7. Expansion Multiple Parameter Lists

   ```scala
   def f(args1)...(argsn) = E
   // when n > 1, is equivalent to 

   def f(args1)...(args_{n - 1}) = {def g(argsn) = E; g}
   // for short
   def f(args1)...(args_{n - 1}) = (argsn => E)
   // repeat then got
   def f = (args1 => (args2 => ...(argsn => E)...))
   // this style of definition and function application is called Currying

   ```

8. Exercise

   ```scala
   // product function calculate product of given interval
   def product(f: Int => Int)(a: Int, b: Int): Int =
     if (a > b) 1 else f(a) * product(f)(a + 1, b)
   // write factorial in term of product
   def fact(n: Int): Int = product(x=>x)(1, n)
   	
   // more general function for both sum and product
   // my version is more complex
   def g(act: (Int, Int) => Int, acc: Int)(f: Int => Int)(a: Int, b: Int): Int = 
   	if (a > b) acc else act(f(a), g(act, acc)(f)(a + 1, b))

   // martin's version
   def mapReduce(f: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int): Int =
     if (a > b) zero else combine(f(a), mapReduce(f, combine, zero)(a + 1, b))
   ```

9. Example: Fixed point

   ```scala
   def abs(x: Double) = if (x > 0) x else -x
   val tolerance = 0.0001
   def isCloseEnough(x: Double, y: Double) =
     abs((x - y) / x) / x < tolerance
   def fixedPoint(f: Double => Double)(firstGuess: Double) = {
     def iterate(guess: Double): Double = {
       val next = f(guess)
       if (isCloseEnough(guess, next)) next
       else iterate(next)
     }
     iterate(firstGuess)
   }

   fixedPoint(x => 1 + x / 2)(1)

   // infinite loop
   def sqrt_infloop(x: Double) =
     fixedPoint(y => x / y)(1.0)

   //sqrt(3)

   def sqrt(x: Double) =
     fixedPoint(y => (y + x / y) / 2)(1.0)

   sqrt(3)
   ```

10. Exercise: 

  ```scala
  def averageDump(f: Double => Double)(x: Double) = (x + f(x)) / 2
  // my version
  def sqrt2(x: Double) =
    fixedPoint(averageDump(y => x / y))(1.0)
  ```

11. class

   ```scala
   class Rational(x: Int, y: Int) {
     def numer = x
     def denom = y

     def add(that: Rational) = new Rational(
         numer * that.denom + that.numer * denom,
         denom * that.denom)

     override def toString = numer + "/" + denom

     def neg = new Rational(-numer, denom)

     //def sub(that: Rational) = new Rational(
     //  numer * that.denom - that.numer * denom,
     //  denom * that.denom
     //)
     def sub(that: Rational) = add(that.neg)
   }

   val x = new Rational(1, 3)
   val y = new Rational(5, 7)
   val z = new Rational(3, 2)
   x.add(y)
   x.sub(y)
   x.sub(y).sub(z)
   ```

12. class new version

   ```scala
   class Rational(x: Int, y: Int) {
     require(y != 0, "denominator must be nonzero")

     def this(x: Int) = this(x, 1)

     private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
     private val g = gcd(x, y)
     def numer = x / g
     def denom = y / g

     //def add(that: Rational) = new Rational(
     def + (that: Rational) = new Rational(
         numer * that.denom + that.numer * denom,
         denom * that.denom)

     override def toString = numer + "/" + denom

     // def neg = new Rational(-numer, denom)
     def unary_- : Rational = new Rational(-numer, denom)
     //def sub(that: Rational) = new Rational(
     //  numer * that.denom - that.numer * denom,
     //  denom * that.denom
     //)
     def - (that: Rational) = this + -that

     //def less(that: Rational) = numer * that.denom < that.numer * denom
     def < (that: Rational) = numer * that.denom < that.numer * denom

     def max(that: Rational) = if (this < (that)) that else this
   }
   ```

   ​

13. Exercise

   (all letter)

   |

   ^

   &

   < >

   = !

   :

   \+ \-

   \* / %

   other

   ```scala
   a + b ^? c ?^ d less a ===> b | c
   a + b ^? (c ?^ d) less a ===> b | c
   (a + b) ^? (c ?^ d) less (a ===> b) | c
   ((a + b) ^? (c ?^ d)) less (a ===> b) | c
   ((a + b) ^? (c ?^ d)) less ((a ===> b) | c)





   ```

   ​
