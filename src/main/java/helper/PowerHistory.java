package helper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Power 额外构造参数的静态历史栈，供 undomod 等需要按
 * {@code (AbstractCreature owner, int amount)} 反射重建 Power 的模组使用。
 * <p>
 * 每个 Power 类各自维护一个后进先出的栈：Power 被移除时压入它的额外构造参数，
 * 之后用两参构造重建同类 Power 时弹出最近一次记录的参数。历史记录在战斗开始时清空，
 * 战斗结束时也会清空，避免数据跨战斗残留。
 * <p>
 * 这里只保存额外参数，{@code amount} 由调用方通过构造参数传入。
 */
public final class PowerHistory {
    // 按 Power 类型分栈，避免不同类交错移除时取到错误记录
    private static final Map<Class<?>, Deque<Object[]>> HISTORY = new HashMap<>();

    private PowerHistory() {
    }

    /**
     * 记录一次 Power 移除时的额外构造参数。
     */
    public static void push(Class<?> powerClass, Object... data) {
        Deque<Object[]> stack = HISTORY.get(powerClass);
        if (stack == null) {
            stack = new ArrayDeque<>();
            HISTORY.put(powerClass, stack);
        }
        stack.push(data);
    }

    /**
     * 取出最近一次记录的额外构造参数；没有记录时返回 null。
     */
    public static Object[] pop(Class<?> powerClass) {
        Deque<Object[]> stack = HISTORY.get(powerClass);
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

    /**
     * 清空全部历史记录，应在战斗结束时调用。
     */
    public static void clear() {
        HISTORY.clear();
    }
}
