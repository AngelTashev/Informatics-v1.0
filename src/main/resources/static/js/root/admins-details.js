
const URLS = {
    admins: '/root-admin-panel/admins-rest',
};
const createRow = ({id, fullName, username, email, points}) => {

    let mainDetails =
           `<td>${fullName}</td>
           <td>${username}</td>
           <td>${email}</td>
           <td>${points}</td>`;
    let link =
        `<td>
            <a href="/root-admin-panel/admins/demote-admin/${id}">Demote to student</a>
        </td>`;

    return `<tr>${mainDetails}${link}</tr>`

};

fetch(URLS.admins)
    .then(response => response.json())
    .then(admins => {
        let result = '';
        admins.forEach((admin) => {

            const adminString = createRow(admin);
            result += adminString;
        });
        document.getElementById('all-admins')
            .innerHTML = result;

    });