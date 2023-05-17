SRC_DIR := src
OUT_DIR := .

SRCS := $(wildcard $(SRC_DIR)/*.java)

CLS := $(SRCS:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)

JC := javac
JCFLAGS := -d $(OUT_DIR)/ -cp $(SRC_DIR)/

jlox: $(SRCS)
	$(JC) $(JCFLAGS) $^
