export const dialogActionButtonStyles = theme => ({
	borderRadius: "12px",
	padding: "5px 12px",
	minHeight: "32px",
	textTransform: "uppercase",
	boxShadow: "none",
	border: theme.palette.mode === "dark" ? "1px solid rgba(148,163,184,0.18)" : "1px solid rgba(15,23,42,0.08)",
	background: theme.palette.mode === "dark" ? "rgba(255,255,255,0.03)" : "rgba(248,251,255,0.82)",
	color: theme.palette.mode === "dark" ? "rgba(226,232,240,0.92)" : "rgba(15,23,42,0.82)",
	transition: "background-color 120ms ease, border-color 120ms ease, color 120ms ease",
	"&:hover": {
		border: theme.palette.mode === "dark" ? "1px solid rgba(144,202,249,0.28)" : "1px solid rgba(25,118,210,0.16)",
		background: theme.palette.mode === "dark" ? "rgba(144,202,249,0.10)" : "rgba(25,118,210,0.07)",
		boxShadow: "none",
	},
	"&.Mui-disabled": {
		color: theme.palette.mode === "dark" ? "rgba(148,163,184,0.44)" : "rgba(100,116,139,0.50)",
		border: theme.palette.mode === "dark" ? "1px solid rgba(71,85,105,0.24)" : "1px solid rgba(148,163,184,0.24)",
		background: theme.palette.mode === "dark" ? "rgba(15,23,42,0.16)" : "rgba(248,250,252,0.56)",
		opacity: 1,
		cursor: "not-allowed",
		pointerEvents: "auto",
	},
	"&.Mui-disabled:hover": {
		border: theme.palette.mode === "dark" ? "1px solid rgba(71,85,105,0.24)" : "1px solid rgba(148,163,184,0.24)",
		background: theme.palette.mode === "dark" ? "rgba(15,23,42,0.16)" : "rgba(248,250,252,0.56)",
		boxShadow: "none",
	},
});

export const dialogPrimaryButtonStyles = theme => ({
	borderRadius: "12px",
	padding: "5px 12px",
	minHeight: "32px",
	textTransform: "uppercase",
	boxShadow: "none",
	border: theme.palette.mode === "dark" ? "1px solid rgba(144,202,249,0.42)" : "1px solid rgba(25,118,210,0.22)",
	background: theme.palette.mode === "dark" ? "linear-gradient(180deg, rgba(144,202,249,0.24) 0%, rgba(96,165,250,0.18) 100%)" : "linear-gradient(180deg, rgba(25,118,210,0.16) 0%, rgba(25,118,210,0.11) 100%)",
	color: theme.palette.mode === "dark" ? "rgba(248,250,252,0.99)" : theme.palette.primary.dark,
	transition: "background-color 120ms ease, border-color 120ms ease, color 120ms ease",
	"&:hover": {
		border: theme.palette.mode === "dark" ? "1px solid rgba(144,202,249,0.54)" : "1px solid rgba(25,118,210,0.30)",
		background: theme.palette.mode === "dark" ? "linear-gradient(180deg, rgba(144,202,249,0.30) 0%, rgba(96,165,250,0.24) 100%)" : "linear-gradient(180deg, rgba(25,118,210,0.22) 0%, rgba(25,118,210,0.16) 100%)",
		boxShadow: "none",
	},
	"&.Mui-disabled": {
		color: theme.palette.mode === "dark" ? "rgba(148,163,184,0.48)" : "rgba(100,116,139,0.54)",
		border: theme.palette.mode === "dark" ? "1px solid rgba(71,85,105,0.26)" : "1px solid rgba(148,163,184,0.26)",
		background: theme.palette.mode === "dark" ? "linear-gradient(180deg, rgba(30,41,59,0.26) 0%, rgba(15,23,42,0.22) 100%)" : "linear-gradient(180deg, rgba(226,232,240,0.62) 0%, rgba(241,245,249,0.78) 100%)",
		opacity: 1,
		cursor: "not-allowed",
		pointerEvents: "auto",
	},
	"&.Mui-disabled:hover": {
		border: theme.palette.mode === "dark" ? "1px solid rgba(71,85,105,0.26)" : "1px solid rgba(148,163,184,0.26)",
		background: theme.palette.mode === "dark" ? "linear-gradient(180deg, rgba(30,41,59,0.26) 0%, rgba(15,23,42,0.22) 100%)" : "linear-gradient(180deg, rgba(226,232,240,0.62) 0%, rgba(241,245,249,0.78) 100%)",
		boxShadow: "none",
	},
});
