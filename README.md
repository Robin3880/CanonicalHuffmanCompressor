# HuffmanFileCompressor 

This is a file compressor and decompressor which I built to help me learn Java.

It uses canonical Huffman coding to compress text files into `.huf` format files and decompress them back.

Requires Java Runtime Environment to be used


## Set Up 
```
git clone https://github.com/Robin3880/CanonicalHuffmanCompressor.git
cd CanonicalHuffmanCompressor
```
---

## Compressing

To compress a file run:
```
java Compress sample.txt
```
This will create the compressed file named `sample.txt.huf` in the same directory.

---

## Decompressing

To decompress a  file run:
```
java Decompress sample.txt.huf
```
This will recreate the original file `sample.txt` in the directory.
