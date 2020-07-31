
const URLS = {
    admins: '/root-admin-panel/students-rest',
};
const createRow = ({id, fullName, username, email, points}) => {

    let mainDetails =
           `<td>${fullName}</td>
           <td>${username}</td>
           <td>${email}</td>
           <td>${points}</td>`;
    let link =
        `<td>
            <a href="/root-admin-panel/students/promote-student/${id}">Promote to teacher</a>
        </td>`;

    return `<tr>${mainDetails}${link}</tr>`

};

fetch(URLS.admins)
    .then(response => response.json())
    .then(students => {
        let result = '';
        students.forEach((student) => {

            const adminString = createRow(student);
            result += adminString;
        });
        document.getElementById('all-students')
            .innerHTML = result;

    });