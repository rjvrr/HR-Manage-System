//Variables
const input = document.getElementById("taskInput");
const addBtn = document.getElementById("addBtn");
const taskList = document.getElementById("taskList");

//  EVENT LISTENER — runs code when button is clicked
addBtn.addEventListener("click", addTask);

//  FUNCTION — adds a new task to the list
function addTask() {
  // Get the input value (what user typed)
  const taskText = input.value.trim();

  // Check if the input is empty
  if (taskText === "") {
    alert("Please enter a task!");
    return; // stop the function
  }

  // Create <li> element for the new task
  const li = document.createElement("li");

  // Create <span> to hold the text
  const span = document.createElement("span");
  span.textContent = taskText;

  

  // Create delete button 
  const delBtn = document.createElement("button");
  delBtn.textContent = "Delete";
  delBtn.addEventListener("click", () => {
    li.remove(); // remove the whole task
  });

  // Add the span and delete button inside the <li>
  li.appendChild(span);
  li.appendChild(delBtn);

  // Add the <li> into the <ul> (task list)
  taskList.appendChild(li);

  // Clear the input box for the next task
  input.value = "";
}