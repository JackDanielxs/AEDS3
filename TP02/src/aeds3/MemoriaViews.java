package aeds3;

import java.util.List;
import java.util.Stack;

import view.View;

public class MemoriaViews {
    private static Stack<View> memoriaDasViews = new Stack<>();
    public static View pop() { return memoriaDasViews.pop(); }
    public static void push(View view) { memoriaDasViews.push(view); }
    public static void reset() { memoriaDasViews.clear(); }
    public static List<View> toList() { return memoriaDasViews.subList(0, memoriaDasViews.size()); }
}
