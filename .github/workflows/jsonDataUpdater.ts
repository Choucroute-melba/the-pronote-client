const fs = require('fs');

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
const updateData: UpdateData = require("../../data/updates.json");
console.log(updateData);
const entListData: EntListData = require("../../data/entList.json");
console.log(entListData);

// update the data
updateData.last_updated = new Date().toISOString();
entListData.last_updated = new Date().toISOString();

// write the data back to the file
fs.writeFileSync('../../data/updates.json', JSON.stringify(updateData));
fs.writeFileSync('../../data/entList.json', JSON.stringify(entListData));

console.log("Done");