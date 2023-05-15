#include <stdlib.h>
#include <stdio.h>

#include "doubly_linked.h"

dlnode *dlcreate()
{
    dlnode *head = malloc(sizeof(dlnode));

    head->next = NULL;
    head->prev = NULL;

    head->val = (void *) -1;

    return head;
}

void dldestroy(dlnode *head)
{
    dlnode *next_node = head->next;
    
    while (next_node != NULL)
    {
        free(head);
        head = next_node;
        next_node = next_node->next;
    }
    free(head);
}

int dlinsert(dlnode *before, void *data)
{
    if (before == NULL) return -1;
    
    dlnode *new_node = malloc(sizeof(dlnode));

    new_node->prev = before;
    new_node->next = before->next;

    before->next = new_node;

    return 1;
}

dlnode *dlfind(dlnode* head, void *data)
{
    do { head = head->next; if (head->val == data) break; } 
    while (head->next != NULL);

    if (head->val == data) return head;
    return NULL;
}

int dlremove(dlnode *head, void *data)
{
    if (data == head->val) return -1; // cannnot delete head

    dlnode *to_remove = dlfind(head, data);
    if (to_remove == NULL) return -1;
    
    // to_remove must have prev, to_remove is not null
    to_remove->prev->next = to_remove->next;
    if (to_remove->next != NULL) to_remove->next->prev = to_remove->prev;

    free(to_remove);

    return 1;
}
