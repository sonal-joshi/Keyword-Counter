JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		fibonode.java\
        fibonacciheap.java\
        keywordcounter.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.clas

	