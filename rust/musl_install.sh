mkdir musldist
PREFIX=$(pwd)/musldist
# Build musl
curl -O http://www.musl-libc.org/releases/musl-1.1.10.tar.gz
tar xf musl-1.1.10.tar.gz
cd musl-1.1.10/
./configure --disable-shared --prefix=$PREFIX
make
make install
cd ..

curl -O http://llvm.org/releases/3.7.0/llvm-3.7.0.src.tar.xz
tar xf llvm-3.7.0.src.tar.xz
cd llvm-3.7.0.src/projects/
curl http://llvm.org/releases/3.7.0/libunwind-3.7.0.src.tar.xz | tar xJf -
mv libunwind-3.7.0.src libunwind
mkdir libunwind/build
cd libunwind/build
cmake -DLLVM_PATH=../../.. -DLIBUNWIND_ENABLE_SHARED=0 ..
make
cp lib/libunwind.a $PREFIX/lib/
cd ../../../../

git clone https://github.com/rust-lang/rust.git muslrust
cd muslrust
./configure --target=x86_64-unknown-linux-musl --musl-root=$PREFIX --prefix=$PREFIX
make
make install
cd ..
