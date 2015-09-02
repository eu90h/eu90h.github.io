---
layout: post
title: "Yet Another Post About Diffie-Hellman"
date: 2015-09-01
categories: cryptography number-theory
---
## Introduction

The problem of sharing a secret key over an insecure channel began receiving attention in the late 1960s, early 1970s, apparently first by the GCHQ and later by the academic cryptographic community.

One solution is called the Diffie-Hellman key exchange. In this scheme, a secret key is constructed by two parties (traditionally Alice and Bob) communicating over an insecure channel.

How is it possible to do this in the face of an adversary who is listening to the messages sent between Alice and Bob?
<!--more-->

## Enter the Galois Field

The answer comes from, perhaps suprisingly, from number theory. Under proper conditions (to be discussed), it is believed to be prohibitively difficult to compute logarithms in the finite field GF(q). By exploiting this difficulty, we can share a key.

But how does it work?

## The Process
Let q be a prime and g be a generator for the finite field GF(q).

* Alice and Bob both generate random numbers a and b, respectively, chosen uniformly from {% raw %} $$ \{1,2,...,q-1\} $$ {% endraw %}. These numbers are meant to be kept secret.

* Alice computes {% raw %} $$ Y_a = g^a \mod{q} $$ {% endraw %} and Bob computes {% raw %} $$ Y_b = g^b \mod{q} $$ {% endraw %}

* Alice and Bob exchange their Y's computed in the last step, and compute their shared secret key:
{% raw %} $$ k = Y_a^b = Y_b^a \mod{q} $$ {% endraw %}.

They may now use k as a key to encrypt their communications.

## What Makes it Safe?

Suppose Eve is eavesdropping on Alice and Bob's communication. Eve knows both {% raw %}$$Y_a$$ {% endraw %} and {% raw %} $$Y_b$$ {% endraw %}, in addition to q and g. In order for Eve to compute their secret key k, she must compute {% raw %} $$Y_a^{\log_g{Y_b}}\mod{q}$$ {% endraw %}. 

In the measured words of Diffie and Hellman {% sidenote 1 'see the original paper [here](https://ee.stanford.edu/~hellman/publications/24.pdf).' %},

> "We thus see that if logs mod q are easily computed the sysstem can be broken. 
> While we do not currently have a proof of the converse (i.e., that the system is secure if logs mod q are difficult to compute), neither do we see any way to compute k from {% raw %} $$Y_a$$ {% endraw %} and {% raw %} $$Y_b$$ {% endraw %} without first obtaining either a or b [the randomly selected exponents of step 1]."

## But is it really safe?

The particular variant of the Diffie-Hellman protocol described above could be weak, depending on the chosen parameters. The TLS Logjam attack showed that keys generated with 512-bit primes could be bruteforced. 

One characteristic of a good prime number p is p-1 having large prime factors. In 1978, [Pohlig and Hellman demonstrated](https://www-ee.stanford.edu/~hellman/publications/28.pdf) an {% raw %} $$O(log^2{p})$$ {% endraw %} algorithm for calculating discrete logarithms when p-1 has only small prime factors.

The IETF has a [list](https://tools.ietf.org/html/rfc3526) of recommended prime number/generator pairs to use in Diffie-Hellman exchanges, although in light of the logjam attack, you should use >= 2048-bit primes only.

These days, for maximum security, the protocol described above shouldn't be used. Instead, you should use [elliptic curve Diffie-Hellman](https://en.wikipedia.org/wiki/Elliptic_curve_Diffie%E2%80%93Hellman). Regardless, the original Diffie-Hellman algorithm is an important thing to learn from.

## Implementation

If you wish to see an implementation in Racket, see this [gist](https://gist.github.com/eu90h/9dd9c2a1051dedfd137e)
