import React from "react";
import {Chip} from "@mui/material";

const DropzoneArea = (props) => {
	const inputRef = React.useRef(null);
	const [files, setFiles] = React.useState([]);

	const handleSelect = (fileList) => {
		const selectedFiles = Array.from(fileList || []);
		const validFiles = selectedFiles.filter(file => _isValidFile(file, props));
		const limitedFiles = validFiles.slice(0, props.filesLimit || validFiles.length);
		const rejectedFile = selectedFiles.find(file => !_isValidFile(file, props));
		if (rejectedFile != null && props.showAlerts !== false) window.alert(_rejectMessage(rejectedFile, props));
		setFiles(limitedFiles);
		if (limitedFiles.length > 0 && props.onChange != null) props.onChange(limitedFiles);
	};

	const handleDrop = (event) => {
		event.preventDefault();
		handleSelect(event.dataTransfer.files);
	};

	const handleDelete = (file) => {
		const updatedFiles = files.filter(item => item !== file);
		setFiles(updatedFiles);
		if (props.onDelete != null) props.onDelete(file);
	};

	const Icon = props.Icon;
	const displayedFiles = files.length > 0 ? files : _fileObjects(props.fileObjects);
	const rootClassName = ["MuiDropzoneArea-root", props.dropzoneClass].filter(Boolean).join(" ");
	const iconClassName = ["MuiDropzoneArea-icon", props.dropzoneIconClass].filter(Boolean).join(" ");

	return (
		<div className={rootClassName}
			 style={_dropzoneStyle()}
			 onDragOver={(event) => event.preventDefault()}
			 onDrop={handleDrop}
			 onClick={() => inputRef.current && inputRef.current.click()}>
			<input ref={inputRef}
				   type="file"
				   style={{display: "none"}}
				   accept={(props.acceptedFiles || []).join(",")}
				   multiple={(props.filesLimit || 1) > 1}
				   onChange={(event) => handleSelect(event.target.files)}/>
			{Icon != null && <Icon className={iconClassName}/>}
			{props.dropzoneText && <p className={props.dropzoneParagraphClass}>{props.dropzoneText}</p>}
			{props.showPreviewsInDropzone && displayedFiles.length > 0 && (
				<div style={{width: "100%", display: "flex", flexWrap: "wrap", justifyContent: "center", gap: "6px", padding: "8px"}}>
					{props.previewText && <div style={{width: "100%", textAlign: "center", fontSize: "10pt"}}>{props.previewText}</div>}
					{displayedFiles.map((file, index) => (
						<Chip key={index}
							  size="small"
							  label={props.showFilenames !== false ? _fileName(file) : ""}
							  onDelete={() => handleDelete(file)}/>
					))}
				</div>
			)}
		</div>
	);
};

const _dropzoneStyle = () => ({
	border: "1px dashed",
	cursor: "pointer",
	display: "flex",
	flexDirection: "column",
	alignItems: "center",
	justifyContent: "center",
	boxSizing: "border-box",
	textAlign: "center",
});

const _fileObjects = (fileObjects) => {
	if (fileObjects == null) return [];
	return fileObjects.map(item => {
		if (item == null) return null;
		if (item instanceof File) return item;
		if (item.file instanceof File) return item.file;
		if (item.file != null && item.file.value instanceof File) return item.file.value;
		if (item.value instanceof File) return item.value;
		return item.file || item.value || item;
	}).filter(Boolean);
};

const _fileName = (file) => {
	if (file == null) return "";
	if (file.name != null) return file.name;
	if (file.file != null && file.file.name != null) return file.file.name;
	if (file.file != null && file.file.value != null && file.file.value.name != null) return file.file.value.name;
	return "";
};

const _isValidFile = (file, props) => {
	if (file == null) return false;
	if (props.maxFileSize != null && file.size > props.maxFileSize) return false;
	return _isAccepted(file, props.acceptedFiles);
};

const _isAccepted = (file, acceptedFiles) => {
	if (acceptedFiles == null || acceptedFiles.length === 0) return true;
	return acceptedFiles.some(accepted => {
		if (accepted == null || accepted === "") return true;
		if (accepted.endsWith("/*")) return file.type != null && file.type.startsWith(accepted.slice(0, -1));
		if (accepted.startsWith(".")) return file.name != null && file.name.toLowerCase().endsWith(accepted.toLowerCase());
		return file.type === accepted;
	});
};

const _rejectMessage = (file, props) => {
	if (props.getDropRejectMessage != null) return props.getDropRejectMessage(file);
	return "Could not upload file";
};

export { DropzoneArea };
export default DropzoneArea;
