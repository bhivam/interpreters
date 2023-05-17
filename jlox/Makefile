SRC_DIR := lox
TOOL_DIR := tool
OUT_DIR := .

SRCS := $(wildcard $(SRC_DIR)/*.java)
TOOL := $(wildcard $(TOOL_DIR)/*.java)

JC := javac
JCFLAGS := -d $(OUT_DIR)/ -cp $(SRC_DIR)/
JCTF := -d $(OUT_DIR)/ -cp $(TOOL_DIR)/


jlox: $(SRCS) $(TOOL)
	$(JC) $(JCFLAGS) $(SRCS)
	$(JC) $(JCTF) $(TOOL)
