package calculos;

import java.util.concurrent.RecursiveTask;
import java.util.Arrays;

public class MergeSortTask extends RecursiveTask<int[]> {
    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 1000; // Umbral para cambiar a secuencial

    public MergeSortTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected int[] compute() {
        if (end - start <= THRESHOLD) {
            return sequentialSort(Arrays.copyOfRange(array, start, end));
        } else {
            int mid = (start + end) / 2;

            MergeSortTask leftTask = new MergeSortTask(array, start, mid);
            MergeSortTask rightTask = new MergeSortTask(array, mid, end);

            invokeAll(leftTask, rightTask);

            int[] leftResult = leftTask.join();
            int[] rightResult = rightTask.join();

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
