const fs = require('fs');

// Get the list of changed files from command line argument
const changedFilesRaw = process.argv[2].replace("[", "").replace("]", "");
const changedFiles = changedFilesRaw.split(" ").map((file: string) => file.trim());
console.log(changedFiles);

const rootDir = "D:\\\\Vivien\\\\p\\\\Android\\\\ThePronoteClient";//process.cwd();
console.log(rootDir);

type UpdateData = {
    last_updated: string,
    last_updated_ent_list: string,
    last_updated_changelog: string,
    last_updated_info: string
}

type EntListData = {
    last_updated: string,
    list: string[]
}
// open the file in read / write mode
const updateData: UpdateData = require(rootDir + "/data/updates.json");
const entListData: EntListData = require(rootDir + "/data/entList.json");

let updated = false;
// update the data
if (changedFiles.includes("data/entList.json")) {
    entListData.last_updated = new Date().toISOString();
    console.log(entListData);
    updated = true;
}

if(updated) {
    updateData.last_updated = new Date().toISOString();
    updateData.last_updated_ent_list = entListData.last_updated;
    console.log(updateData);
    fs.writeFileSync(rootDir + "/data/updates.json", JSON.stringify(updateData, null, 2));
    fs.writeFileSync(rootDir + "/data/entList.json", JSON.stringify(entListData, null, 2));
    console.log("Done");
} else
    console.log("Nothing to update");

// write the data back to the file
console.log("Done");