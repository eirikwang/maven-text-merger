File nbFile = new File( basedir, "target/classes/minbundle_nb.properties" );
File enFile = new File( basedir, "target/classes/minbundle_en.properties" );

assert nbFile.isFile()
assert enFile.isFile()

def nb = nbFile.readLines();
def en = enFile.readLines();
assert nb.size() == 3
assert en.size() == 3
assert nb[0].startsWith("a.b=")
assert nb[1].startsWith("a.c=")
assert nb[2].startsWith("b.a=")
assert en[0].startsWith("a.b=")
assert en[1].startsWith("a.c=")
assert en[2].startsWith("b.a=")