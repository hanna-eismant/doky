export const saveFile = async (body, fileName) => {
  try {
    const handleSave = await window.showSaveFilePicker({ suggestedName: fileName });
    const writableStream = await handleSave.createWritable();
    body.pipeTo(writableStream);
  } catch {
    /*
     * Promise returned by window.showSaveFilePicker is rejected if user just close file picker.
     * This is regular case. I don't think that we need to handle this somehow.
     */
  }
};
