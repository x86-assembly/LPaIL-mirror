# LPaIL compiler

a compiler written in java for a custom language called `LPaIL`

# Building

Eventually there will be just a single jar that can be used, but for now:

- use `./gradlew build`: should create something that works, but no guarantees.
- *(RECOMMENDED)* create or modifying a Test, and run using
  `./gradlew test` (test results will be in
  [app/build/reports/tests/test/index.html](app/build/reports/tests/test/index.html) )

## Running

The compiler generates x86_64 nasm assembly.
You can assemble and link it using `nasm -felf64 <yourfile> -o a.o && gcc ./a.o -no-pie -o a.out`

