#
#	Copyright ©2020 WellEngineered.us, all rights reserved.
#	Distributed under the MIT license: https://opensource.org/licenses/MIT
#

cls
$psvt_git_commit_id = $PSVersionTable.GitCommitId

if ($psvt_git_commit_id -eq $null)
{ echo "An error occurred during the operation (GIT commit ID)."; return; }

echo "Using PowerShell: $psvt_git_commit_id" 

$this_dir_path = [System.Environment]::CurrentDirectory

$git_ignore_dir_name = ".gitignore"
$git_ignore_file_path = "$this_dir_path\$git_ignore_dir_name"

$lines = Get-Content $git_ignore_file_path

foreach ($line in $lines)
{
	if ([System.String]::IsNullOrWhiteSpace($line) -or $line.StartsWith("#") -or $line.Contains("[\!]git*"))
	{ continue; }

	$remove_items = Get-ChildItem $this_dir_path -Recurse -Include $line -Force

	foreach ($remove_item in $remove_items)
	{
		echo ("Deleting Git ignored($line) item: " + $remove_item.FullName)

		try { Remove-Item $remove_item.FullName -Recurse -Force } catch { }
	}
}
