#ifndef _DOUBLY_LINKED_H
#define _DOUBLY_LINKED_H

typedef struct dlnode
{
	struct dlnode *prev;
	struct dlnode *next;
	void *val;
} dlnode;

dlnode *dlcreate();
void dldestroy(dlnode*);
int dlinsert(dlnode *, void *);
dlnode *dlfind(dlnode *, void *);
int dlremove(dlnode *, void *);

#endif
