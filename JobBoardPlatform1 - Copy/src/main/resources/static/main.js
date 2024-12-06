// script.js
document.addEventListener("DOMContentLoaded", function() {
  // Simulate loading job listings (you would replace this with a fetch call)
  const jobListingsSection = document.querySelector('.job-listings ul');
  const jobs = [
    { title: "Software Engineer", id: 1 },
    { title: "Data Analyst", id: 2 },
    { title: "Project Manager", id: 3 }
  ];

  jobs.forEach(job => {
    const li = document.createElement('li');
    li.innerHTML = `${job.title} <button onclick="applyForJob(${job.id})">Apply</button>`;
    jobListingsSection.appendChild(li);
  });
});

function applyForJob(jobId) {
  alert(`Applying for job ID: ${jobId}`);
  // Here, you would add the logic to handle job applications
}
