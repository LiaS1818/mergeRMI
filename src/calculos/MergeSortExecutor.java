package calculos;

import java.util.concurrent.Callable;
import java.util.Arrays;

public class MergeSortExecutor implements Callable<int[]> {
    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 1000; // Umbral para cambiar a secuencial


    public MergeSortExecutor(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public int[] call() throws Exception {
        if (end - start <= THRESHOLD) {
            return sequentialSort(Arrays.copyOfRange(array, start, end));
        } else {
            int mid = (start + end) / 2;

            MergeSortExecutor leftTask = new MergeSortExecutor(array, start, mid);
            MergeSortExecutor rightTask = new MergeSortExecutor(array, mid, end);

            int[] leftResult = leftTask.call();
            int[] rightResult = rightTask.call();

            return merge(leftResult, rightResult);
        }
    }

    private int[] sequentialSort(int[] subArray) {
        Arrays.sort(subArray);
        return subArray;
    }

    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length) {
            result[k++] = left[i++];
        }

        while (j < right.length) {
            result[k++] = right[j++];
        }

        return result;
    }
}
