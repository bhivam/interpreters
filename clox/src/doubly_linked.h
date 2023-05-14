#ifndef _DOUBLY_LINKED_H
#define _DOUBLY_LINKED_H

typedef struct dnode
{
	dnode *prev;
	dnode *next;
	void *val;
} dnode;

dnode *create_list();
void destroy_list(dnode*);
int insert(dnode *, void *);
dnode *find(dnode*, void *);
int delete(dnode *);

#endif
