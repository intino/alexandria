export const normalizeHighlight = (value, fallback) => {
    const source = value != null ? value : fallback;
    if (source == null) return null;
    const text = source.textColor != null ? source.textColor : source.text;
    const accent = source.backgroundColor != null ? source.backgroundColor : (source.background != null ? source.background : text);
    return accent != null || text != null ? { text, accent } : null;
};

export const withHighlight = (style, highlight) => {
    const result = { ...(style || {}) };
    if (highlight == null) return result;
    if (highlight.text != null) result.color = highlight.text;
    if (highlight.accent == null) return result;
    result.boxShadow = `inset 0 0 0 1px color-mix(in srgb, ${highlight.accent} 72%, transparent), 0 0 0 2px color-mix(in srgb, ${highlight.accent} 16%, transparent)`;
    return result;
};

export const withHighlightBackground = (style, highlight) => {
    const result = { ...(style || {}) };
    if (highlight == null || highlight.accent == null) return result;
    result.background = `color-mix(in srgb, ${highlight.accent} 14%, transparent)`;
    return result;
};
