---
layout: post
title:  "Adventures in a Foreign Land: Racket and the FFI"
date:   2015-09-15
categories: ffi openssl crypto 
---
I talked briefly about safe primes in my last post on the Diffie-Hellman key exchange protocol. Again, a prime p is a safe prime if (p-1)/2 is also prime. Safe primes are necessary in light of Pohlig & Hellman's work on the DLP, among more recent advances.

Racket's excellent `math/number-theory` module has a procedure `(random-prime max)` which returns a prime between 2 and max. Unfortunately, we can't tell it to generate a prime number with a specified number of bits, nor can tell it to ensure that the prime is safe.

OpenSSL to the rescue!

<!--more-->

Generating Primes using OpenSSL
===============================

OpenSSL has a procedure for prime generation called [BN_generate_prime_ex](https://www.openssl.org/docs/manmaster/crypto/BN_generate_prime.html), which allows us to specify the number of bits as well as safety.

Its signature is:

```int BN_generate_prime_ex(BIGNUM *ret,int bits,int safe, const BIGNUM *add, const BIGNUM *rem, BN_GENCB *cb);```

Alright so lets go through these parameters one-by-one.

* ```BIGNUM* ret```  -  is the destination where the prime is stored.
* ```int bits```  -  self-explanatory.
* ```int safe```  -  set to 0 for a non-safe prime, 1 for a safe prime.
* ```const BIGNUM *add``` and ```const BIGNUM *rem```  -  if add isn't `NULL`, then the generated prime will satisfy the condition `p % add = rem` (if rem is `NULL`, then `p % add = 1`).
* Finally, `BN_GENCB *cb`  -  a callback invoked at various points during the prime generation proccess (we won't be using this)

The function returns 1 if a prime was generated successfully, 0 otherwise.

In order to wrap this, we need a file `libcrypto.rkt` which is used internally by Racket's OpenSSL bindings. This file can be found [here](https://github.com/plt/racket/blob/3e2b916f5be75dc2a90b4562757d45b1a6779555/racket/collects/openssl/libcrypto.rkt).

Let's ignore the contents of this file for now and concentrate on how to write the bindings.

Wrapping
========
For ease of reading, the entire wrapper is located [here](https://gist.github.com/eu90h/e687d208a1a3dc141d0e).
The code is explained below.

We begin with some ctypes:
  `(define _BIGNUM-ptr (_cpointer/null 'BIGNUM))`
  `(define _BN_CTX-ptr (_cpointer/null 'BN_CTX)`
  `(define _BN_GENCB-ptr (_cpointer/null 'BN_GENCB))`

When specifying the interface to C functions with the racket FFI, it can become tedious. Since this is Scheme, we shouldn't ever be writing tedious code!

A little macrology yields the following:

```
(define-syntax-rule (define-libcrypto x type ...)
  (define x 
    (and libcrypto
         (get-ffi-obj 'x libcrypto (_fun type ...) (lambda () #f)))))
```

Now wrapping OpenSSL's BIGNUM funcs is as simple as

```
(define-libcrypto BN_new -> _BIGNUM-ptr)
(define-libcrypto BN_generate_prime_ex _BIGNUM-ptr _int _bool _BIGNUM-ptr _BIGNUM-ptr _BN_GENCB-ptr -> _int)
(define-libcrypto BN_bn2dec _BIGNUM-ptr -> _string)
```

Not too bad! 

OK, so we have our C primitives. Let's cobble them together into a working generator.

```
; generate-prime : Nonnegative-Integer Boolean -> Positive-Integer
; generates an n-bit prime number. If safe is #t then (p-1)/2 is also prime.
(define (generate-prime [bits 2048] [safe? #f])
  (define prime (BN_new))
  (if (= 1 (BN_generate_prime_ex prime bits safe? #f #f #f))
      (string->number (BN_bn2dec prime)) ; I suspect there's a better way
      (raise "OpenSSL failed to generate prime")))
```

The End
=======
Here's a little Rackunit test that verifies the safety of generated primes:

`(require rackunit math/number-theory)`

`(check-true (prime? (/ (sub1 (generate-prime 1024 #t)) 2)))`


The generator is remarkably fast. It has no trouble popping out a 1024-bit safe prime in <100ms on average. Way better than my poor-quality Racket safe prime generator!

Well that excursion wasn't too awful I hope! 
The Racket FFI is actually really easy to work with and learning it has opened new possibilities for me. Now go and try your hand at wrapping some C code -- there's probably a lot of interesting C library wrappers that Racket could benefit from having.

As for me, I'm going to keep wrapping up OpenSSL's more primitive functions. They have an elliptic curve library; I think it'd be cool to be able to do algebraic operations on elliptic curves from Racket for some exploratory number theory.
