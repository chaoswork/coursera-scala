## Data and Abstract

1. example

   ```scala
   abstract class IntSet {
     def incl(x: Int): IntSet
     def contains(x: Int): Boolean
     def union(other: IntSet): IntSet
   }


   class Empty extends  IntSet {
     def contains(x: Int): Boolean = false
     def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
     def union(other: IntSet): IntSet = other
     override def toString: String = "."
   }

   class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
     def contains(x: Int): Boolean =
       if (x < elem) left contains x
       else if (x > elem) right contains x
       else true

     def incl(x: Int): IntSet =
       if (x < elem) new NonEmpty(elem, left incl x, right)
       else if (x > elem) new NonEmpty(elem, left, right incl x)
       else this

     def union(other: IntSet): IntSet = {
       left.union(right).union(other).incl(elem)
     }

     override def toString: String = "{" + left + elem + right + "}"
   }

   val t1 = new NonEmpty(3, new Empty, new Empty)
   val t2 = t1.incl(4)
   val t3 = t2 incl 2

   val r1 = new NonEmpty(7, new Empty, new Empty)
   val r2 = r1.incl(5)
   val r3 = r2.incl(10)
   val r4 = r3.incl(3)

   ```

   ​

2. exercise

   ```scala
   if (true) 1 else false // type is AnyVal 
   ```

3. Polymorphism

   ```scala
   trait List[T] {
     def isEmpty: Boolean
     def head: T
     def tail: List[T]
   }

   class Cons[T](val head: T, val tail: List[T]) extends List[T] {
     def isEmpty: Boolean = false
   }

   class Nil[T] extends List[T] {
     def isEmpty: Boolean = true
     def head: Nothing = throw new NoSuchElementException("Nil.head")
     def tail: Nothing = throw new NoSuchElementException("Nil.tail")
   }

   def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

   def nth[T](n: Int, xs: List[T]): T = {
     if (xs.isEmpty) throw new IndexOutOfBoundsException
     else if (n == 0) xs.head
     else nth(n - 1, xs.tail)
   }

   val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))

   nth(2, list)

   ```

4. ​


