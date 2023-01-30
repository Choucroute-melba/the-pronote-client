const fs = require('fs');

const rootDir = process.cwd();
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

// update the data
updateData.last_updated = new Date().toISOString();
entListData.last_updated = new Date().toISOString();
console.log(updateData);
console.log(entListData);

// write the data back to the file
fs.writeFileSync(rootDir + '/data/updates.json', JSON.stringify(updateData));
fs.writeFileSync(rootDir + '/data/entList.json', JSON.stringify(entListData));

console.log("Done");