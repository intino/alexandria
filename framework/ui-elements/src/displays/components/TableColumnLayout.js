import React from "react";

export const TableColumnContext = React.createContext(null);
export const MIN_COLUMN_WIDTH = 48;

export const columnWidthStyle = width => width == null ? {} : {
    width: width + "px",
    minWidth: width + "px",
    maxWidth: width + "px",
    flex: "0 0 " + width + "px",
    boxSizing: "border-box",
};

// Fragments do not produce cells, so their children share the column index.
export const mapTableColumns = (children, map) => {
    let index = 0;
    const visit = nodes => React.Children.map(nodes, child => {
        if (!React.isValidElement(child)) return child;
        if (child.type === React.Fragment) return React.cloneElement(child, {}, visit(child.props.children));
        return map(child, index++);
    });
    return visit(children);
};
