const CodeBehavior = (function () {
    return {
        clean : (value) => {
            if (value == null) return value;
            if (!value.split) return value;
            return value.split("\\\\n").map(Function.prototype.call, String.prototype.trim).join("\n");
        }
    }
})();

export default CodeBehavior;
