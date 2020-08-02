
const URLS = {
    messages: '/root-admin-panel/messages-rest',
};
const createRow = ({id, fullName, email, message}) => {

    let mainDetails =
           `<td>${fullName}</td>
           <td>${email}</td>
           <td>${message}</td>`;
    let link =
        `<td>
            <a href="/root-admin-panel/messages/delete/${id}">Delete message</a>
        </td>`;

    return `<tr>${mainDetails}${link}</tr>`

};

fetch(URLS.messages)
    .then(response => response.json())
    .then(messages => {
        let result = '';
        messages.forEach((message) => {

            const messageString = createRow(message);
            result += messageString;
        });
        document.getElementById('all-messages')
            .innerHTML = result;

    });