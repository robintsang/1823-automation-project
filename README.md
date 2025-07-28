# 1823 Automation Project

## Git Collaboration Quick Guide

### 1. Clone the Project to Your Local Machine

```bash
git clone https://github.com/robintsang/1823-automation-project.git
```

### 2. Pull the Latest Code

```bash
git pull origin main
```

### 3. Add Your Code or JSON Data

- Place your test code in the appropriate folder (e.g., `src/test/java/...`).
- Place your complaint data JSON in the `complaints/` folder.

### 4. Stage and Commit Your Changes

```bash
git add .
git commit -m "Add my test code and complaint JSON (your name)"
```

**Example:**

Suppose you added `ComplaintSukiTest.java` and `complaint_suki.json`:

```bash
git add src/test/java/hk1823/automation/Utility/selenium/ComplaintSukiTest.java
git add complaints/complaint_suki.json
git commit -m "Add Suki's Selenium test and complaint JSON (Suki)"
```

- `git add <file path>`: Stage your changed or new files.
- `git commit -m "message"`: Commit your changes with a message.

### 5. Push Your Changes to GitHub

```bash
git push origin main
```

**Example:**

Push your latest commit to GitHub:

```bash
git push origin main
```

- This command uploads your local changes to the main branch on GitHub.

### 6. Update Your Local Code Before Pushing

> **Always run `git pull origin main` before `git push` to avoid conflicts!**

### 7. If You See a Conflict

- Git will show a message. Open the conflicted file, follow the instructions to resolve, then:

```bash
git add <conflicted-file>
git commit -m "Resolve conflict"
git push origin main
```

---

## Common Git Commands

| Command | Description | Example |
|---------|-------------|---------|
| Clone project | Clone the repository to your local machine | `git clone ...` |
| Check status | View the current status of your working directory | `git status` |
| Add file(s) | Stage files for commit | `git add .` |
| Commit changes | Commit staged changes with a message | `git commit -m "message"` |
| Pull latest code | Fetch and merge changes from remote repository | `git pull origin main` |
| Push to GitHub | Upload local commits to remote repository | `git push origin main` |

---

## Notes

- Please do **not** push large files (e.g., videos) to the repository.
- If you have any questions, ask Robin!
