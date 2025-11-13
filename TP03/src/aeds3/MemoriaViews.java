package aeds3;

import java.util.List;
import java.util.Stack;

import view.View;

public final class MemoriaViews {
    private static Stack<View> memoria = new Stack<>();
    public static View pop() { return memoria.pop(); }
    public static void push(View view) { memoria.push(view); }
    public static void reset() { memoria.clear(); }
    public static List<View> toList() { return memoria.subList(0, memoria.size()); }
}
