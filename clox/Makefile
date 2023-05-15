ODIR=bin/obj
BIN=bin

CC=gcc
CFLAGS=-I -Wextra -Wall -std=c99 -g -fsanitize=address

DEPS=src/utils.h src/doubly_linked.h

_OBJ=main.o utils.o doubly_linked.o
OBJ = $(patsubst %,$(ODIR)/%,$(_OBJ))


$(ODIR)/%.o: src/%.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

$(BIN)/main: $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS) 

