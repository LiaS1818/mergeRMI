package calculos;

public class SequentialMergeSort {

    public SequentialMergeSort() {

    }


    public void merge(int[] arreglo, int izq, int mitad, int der) {
        //Encontrar el tamaño de los subvectores para unirlos
        int tam1 = mitad - izq + 1; //mitad derecha
        int tam2 = der - mitad; // mitad izquierda

        int[] arrDer = new int[tam1];
        int[] arrIzq = new int[tam2];

        //Copiar los datos de los arrays temporales

        //arreglo de la parte derecha
        for (int i = 0; i < tam1; i++) {
            arrDer[i] = arreglo[izq + i];
        }

        for (int i = 0; i < tam2; i++) {
            arrIzq[i] = arreglo[mitad + i + 1];
        }

        //Indices para iniciar el primer y segundo arreglo
        int i = 0, j = 0;

        //Indice incial del sub-vector auxiliar
        int k = izq;
        while (i < tam2 && j < tam1) {
            if (arrIzq[i] <= arrDer[j]) {
                arreglo[k] = arrIzq[i];
                i++;
            } else {
                arreglo[k] = arrDer[j];
                j++;
            }
            k++;
        }


        // Si quedan partes de un arreglo por ordenar, o sea se quedo sin pareja para comparar
        // Por lo que no termino el iterador a ser igual que n1 o n2
        while (i < tam2) {
            arreglo[k] = arrIzq[i];
            i++;
            k++;
        }

        while (j < tam1) {
            arreglo[k] = arrDer[j];
            j++;
            k++;
        }
    }

    public  void ordena(int[] arreglo, int izq, int der) {
        if (izq < der){
            int mitad = (izq + der) / 2;

            ordena(arreglo, izq, mitad);
            ordena(arreglo, mitad+1, der);
            merge(arreglo, izq, mitad, der);
        }
    }
}
