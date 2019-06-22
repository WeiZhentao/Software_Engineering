#include<iostream>
using namespace std;
#define MaxInt 32737
#define MVNum 30
bool visited[MVNum];           
typedef struct
{
	char vexs[MVNum];           
	int arcs[MVNum][MVNum];     
	int vexnum,arcnum;         
}AMGraph;
class Tu
{
public:
	int LocateVex(AMGraph G,char u);   
	void print(AMGraph G);            
	void CreateUDN(AMGraph &G);       
	void DFS_AM(AMGraph G,int v);     
	void charu(AMGraph &G);            
	void shanchu(AMGraph &G);          
	void ShortestPath_Floyd(AMGraph G);
};
void Tu::CreateUDN(AMGraph &G)
{
	int i,j,k;
	char v1,v2;        
	int w;               
	cout<<"�������ܶ��������ܱ�����"<<endl;
	cin>>G.vexnum>>G.arcnum;        
	cout<<"��������ֵ��"<<endl;
	for(i=0;i<G.vexnum;i++)        
		cin>>G.vexs[i];
	for(i=0;i<G.vexnum;i++)       
		for(j=0;j<G.vexnum;j++)
			G.arcs[i][j]=MaxInt;
    cout<<"�������������2�����㣬�ߵ�Ȩֵ��"<<endl;
	for(k=0;k<G.arcnum;k++)
	{
		cin>>v1>>v2>>w;            
		i=LocateVex(G,v1);         
		j=LocateVex(G,v2);         
		G.arcs[i][j]=w;
		//G.arcs[j][i]=G.arcs[i][j];
	}
	print(G);
}
void Tu::print(AMGraph G)
{
    int i,j;
    cout<<"����Ӧ�����ţ�"<<endl;
	for(i=0;i<G.vexnum;i++)
	    cout<<i<<"\t";
    cout<<endl;
    for(i=0;i<G.vexnum;i++)
        cout<<G.vexs[i]<<"\t";
	cout<<endl<<"������ڽӾ���"<<endl;
	for(i=0;i<G.vexnum;i++)         
	{
		for(j=0;j<G.vexnum;j++)
		{
		    if(G.arcs[i][j]==32737)
		        cout<<"��"<<"\t";
            else
		        cout<<G.arcs[i][j]<<"\t";
		}
		cout<<endl;
	}
}
int Tu::LocateVex(AMGraph G,char ch)
{
    int a;
    for(int i=0;i<G.vexnum;i++)
    {
        if(G.vexs[i]==ch)
            a=i;
    }
    return a;
}
void Tu::DFS_AM(AMGraph G,int v)
{
	int w;
	cout<<v<<"\t";
	visited[v]=true;
	for(w=0;w<G.vexnum;w++)
		if((G.arcs[v][w]!=32737)&&(!visited[w]))
			DFS_AM(G,w);
	
}
void Tu::charu(AMGraph &G)
{
    int m,n;
    int i,j,k;
    char v1,v2;
    int w;
    cout<<"������ӽڵ㣬�ߵĸ���:"<<endl;
    cin>>m>>n;
    cout<<"������ӵĽ��:"<<endl;
    for(i=G.vexnum;i<G.vexnum+m;i++)            
        cin>>G.vexs[i];
    for(i=G.vexnum;i<G.vexnum+m;i++)              
		for(j=0;j<G.vexnum+m;j++)
			G.arcs[i][j]=MaxInt;
    for(i=0;i<G.vexnum+m;i++)                     
		for(j=G.vexnum;j<G.vexnum+m;j++)
			G.arcs[i][j]=MaxInt;
    cout<<"�������������2�����㣬�ߵ�Ȩֵ��"<<endl;
    G.vexnum=G.vexnum+m;         
    G.arcnum=G.arcnum+n;
    for(k=0;k<n;k++)
	{
		cin>>v1>>v2>>w;            
		i=LocateVex(G,v1);         
		j=LocateVex(G,v2);         
		G.arcs[i][j]=w;
		//G.arcs[j][i]=G.arcs[i][j];
	}
    print(G);
}
void Tu::shanchu(AMGraph &G)
{
    int m,n;
    int i,j,k;
    int s1;             
    int low=0;           
    char v1,v2;
    char ch[10];            
    int w;
    cout<<"����ɾ���ڵ㣬�ߵĸ���:"<<endl;
    cin>>m>>n;
    cout<<"����ɾ���Ľ��:"<<endl;
    for(i=0;i<m;i++)
        cin>>ch[i];
    for(k=0;k<m;k++)               
    {
        s1=LocateVex(G,ch[k]);        
        for(i=s1;i<G.vexnum;i++)        
            G.vexs[i]=G.vexs[i+1];
        for(j=0;j<G.vexnum;j++)        
            if(G.arcs[s1][j]!=32737)   low++;

        
        for(i=0;i<s1;i++)                          
            for(j=s1;j<G.vexnum;j++)
                G.arcs[i][j]=G.arcs[i][j+1];
        for(i=s1;i<G.vexnum;i++)                    
            for(j=0;j<s1;j++)
                G.arcs[i][j]=G.arcs[i+1][j];
        for(i=s1;i<G.vexnum;i++)                    
            for(j=s1;j<G.vexnum;j++)
                G.arcs[i][j]=G.arcs[i+1][j+1];
    }
    if(n>0)               
    {
        cout<<"�������������2�����㣬�ߵ�Ȩֵ��"<<endl;
        for(k=0;k<n;k++)
	    {
		    cin>>v1>>v2>>w;            
		    i=LocateVex(G,v1);         
		    j=LocateVex(G,v2);         
		    G.arcs[i][j]=32737;
		    
	    }
    }
    G.vexnum=G.vexnum-m;         
    G.arcnum=G.arcnum-n-low;
    print(G);
}
void Tu::ShortestPath_Floyd(AMGraph G)
{
    int i,j,k;
    int D[G.vexnum][G.vexnum];
    int Path[G.vexnum][G.vexnum];         
    for(i=0;i<G.vexnum;++i)
        for(j=0;j<G.vexnum;++j)
        {
            D[i][j]=G.arcs[i][j];
            if(D[i][j]<MaxInt&&i!=j)
                Path[i][j]=i;
            else
                Path[i][j]=-1;
        }
    for(k=0;k<G.vexnum;k++)
        for(i=0;i<G.vexnum;i++)
            for(j=0;j<G.vexnum;++j)
                if(D[i][k]+D[k][j]<D[i][j])
                {
                    D[i][j]=D[i][k]+D[k][j];
                    Path[i][j]=Path[k][j];
                }
    cout<<"�����������������������������·��������������������������"<<endl;
    cout<<"����ڵ�ı�ţ�"<<endl;
    int x,y,s[20];         
    cin>>x>>y;
    s[0]=y;
    cout<<"����������·�����ȣ�"<<endl;
    cout<<D[x][y]<<endl;
    cout<<"����������·����"<<endl;
    i=1;
    while(Path[x][y]!=x)
    {
        s[i]=Path[x][y];
        i++;
        y=Path[x][y];
    }
    s[i]=x;
    while(i>=0)
    {
        cout<<s[i]<<"  ";
        i--;
    }
}
int main()
{
	Tu tu;          
	AMGraph G;      
	tu.CreateUDN(G);
	cout<<"�����붥���ţ��Ӹñ�ſ�ʼ������0--n-1����"<<endl;
	int v;
	cin>>v;
	for(int i=0;i<G.vexnum;i++)
        visited[i]=false;
    cout<<"�����ȱ������ı�ţ�"<<endl;
	tu.DFS_AM(G,v);
	cout<<endl;
	tu.charu(G);
	tu.shanchu(G);
	tu.ShortestPath_Floyd(G);
	return 0;
}
