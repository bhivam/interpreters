#include <stdio.h>
#include <stdlib.h>

#include "doubly_linked.h"

int main(int argc, char **argv) 
{
    dlnode *HEAD = dlcreate();

    int num = 10;

    dlinsert(HEAD, &num);
    dlinsert(HEAD, &num);

    dlremove(HEAD, &num);
    dlremove(HEAD, &num);

    dldestroy(HEAD);
    
    return EXIT_SUCCESS;
}
