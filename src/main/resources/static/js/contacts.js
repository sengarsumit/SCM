const baseURL="http://localhost:8080";
const viewContactModal=document.getElementById('view_contact_modal');
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};
// instance options object
const instanceOptions = {
    id: 'view_contact_modal',
    override: true
};

const contactModal=new Modal(viewContactModal,instanceOptions);

function  openContactModal(){
    contactModal.show();
}
function closeContactModal(){
    contactModal.hide();
}
async function loadContactdata(id)
{
console.log(id);
try{
    const data=await (await fetch(`${baseURL}/api/contacts/${id}`)).json();
    console.log(data);
    document.querySelector('#contact_name').innerHTML=data.name;
    document.querySelector('#contact_email').innerHTML=data.email;
    document.querySelector("#contact_image").src = data.picture;
    document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
    const contactFavorite = document.querySelector("#contact_favorite");
    if (data.favorite) {
        contactFavorite.innerHTML =
            "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
    } else {
        contactFavorite.innerHTML = "Not Favorite Contact";
    }
    document.querySelector("#contact_address").innerHTML = data.address;
    document.querySelector("#contact_about").innerHTML = data.description;
    document.querySelector("#contact_website").href = data.websiteLink;
    document.querySelector("#contact_website").innerHTML = data.websiteLink;
    document.querySelector("#contact_linkedIn").href = data.LinkedinLink;
    document.querySelector("#contact_linkedIn").innerHTML = data.LinkedinLink;

    openContactModal()

}
catch (error)
{
    console.log(error);
}
}


async function deleteContact(id)
{
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contact/delete/`+id;
            window.location.replace(url);
        }
    });
}